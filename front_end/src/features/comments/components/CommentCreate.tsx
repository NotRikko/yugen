import { useForm } from "react-hook-form"
import { zodResolver } from "@hookform/resolvers/zod"
import * as z from "zod"
import { Button } from "@/shared/ui/button"
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormMessage,
} from "@/shared/ui/form"
import { Textarea } from "@/shared/ui/textarea"
import { commentApi } from "../api/commentApi"

import { useUser } from "@/features/user/UserProvider";

interface CommentCreateProps {
    postId: number;
}
const formSchema = z.object({
    content: z.string().min(1, "Comment cannot be empty").max(280, "Comment must be at most 280 characters"),
});

export default function CommentCreate( { postId } : CommentCreateProps) {
    const { user } = useUser();

    const form = useForm<z.infer<typeof formSchema>>({
        resolver: zodResolver(formSchema),
        defaultValues: {
            content: "",
        }
    })

    if (user.isGuest) {
        return(
            <div className="w-5/6 mx-auto p-4 border">
                <p className="text-red-500">
                    Only users can comment. Please register as a user to comment.
                </p>
            </div>
        );
    }

    async function onSubmit(values: z.infer<typeof formSchema>) {
        try {
            await commentApi.createComment({
            userId: user.id,
            postId,
            content: values.content,
            });
        
            console.log("Post created successfully");
        } catch (error) {
            console.error("Error creating post", error);
        }
    }

    return (
        <Form {...form} >
            <form onSubmit={form.handleSubmit(onSubmit)} className="w-full mb-8 space-y-8 py-8 w-5/6 mx-auto p-4 border rounded-lg shadow-md bg-white">
                <FormField
                    control={form.control}
                    name="content"
                    render={({ field }) => (
                    <FormItem>
                        <FormControl>
                            <Textarea 
                                placeholder="What's up with your new work?"
                                className="h-20"
                                {...field} 
                            />
                        </FormControl>
                        <FormMessage />
                    </FormItem>
                    )}
                />                
            <Button type="submit">Submit</Button>
            </form>
        </Form>
    )
}